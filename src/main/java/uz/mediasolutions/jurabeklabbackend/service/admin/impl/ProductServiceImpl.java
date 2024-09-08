package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.entity.Product;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ProductRepository;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.ProductService;
import uz.mediasolutions.jurabeklabbackend.service.common.impl.FileServiceImpl;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("adminProductService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FileServiceImpl fileService;

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        Page<ProductDTO> allProducts = productRepository.findAllWithSearch(search, PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts);
    }

    @Override
    @Transactional
    public ResponseEntity<?> add(MultipartFile file) {
        if (file.isEmpty()) {
            throw RestException.restThrow("File cannot be empty", HttpStatus.BAD_REQUEST);
        }

        try {
            saveDataFromExcel(file.getInputStream());
            return ResponseEntity.status(HttpStatus.CREATED).body(Rest.CREATED);
        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
        );
        try {
            fileService.deleteFile(product.getImageUrl());
            product.setDeleted(true);
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
        } catch (Exception e) {
            throw RestException.restThrow("Delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> editImage(Long id, String imageUrl) {
        Product product = productRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
        );
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                fileService.deleteFile(product.getImageUrl());
            } catch (Exception e) {
                throw RestException.restThrow("Image delete failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            product.setImageUrl(imageUrl);
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
        }
        throw RestException.restThrow("Image not found", HttpStatus.NOT_FOUND);
    }

    @Transactional
    protected void saveDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            List<Product> products = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() != 0) {

                    String name = getCellValue(row, 1);
                    String price = getCellValue(row, 4);

                    if (price.isEmpty()) {
                        price = "0";
                    }

                    price = price.replaceAll(",", "");

                    BigDecimal newPrice = BigDecimal.valueOf(Long.parseLong(price)).multiply(BigDecimal.valueOf(1.2));
                    if (!name.isEmpty() && !productRepository.existsByNameAndDeletedFalse(name)) {
                        Product product = Product.builder()
                                .price(newPrice)
                                .name(name)
                                .deleted(false)
                                .build();

                        products.add(product);
                    } else if (productRepository.existsByNameAndDeletedFalse(name)) {
                        Product product = productRepository.findByNameAndDeletedFalse(name);
                        product.setPrice(newPrice);
                        products.add(product);
                    }
                }
            }
            productRepository.saveAll(products);

        } catch (Exception e) {
            throw RestException.restThrow("Excel file could not be read", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString(); // Bu yerda turli xil formatlarni tekshirish va ularni qayta ishlash zarur bo'lishi mumkin
        }
        return ""; // Agar cell mavjud bo'lmasa, bo'sh string qaytarish
    }
}
