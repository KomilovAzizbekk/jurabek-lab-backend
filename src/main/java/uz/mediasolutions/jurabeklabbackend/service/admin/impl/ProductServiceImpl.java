package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.mediasolutions.jurabeklabbackend.entity.Constants;
import uz.mediasolutions.jurabeklabbackend.entity.Product;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.ProductEditDTO;
import uz.mediasolutions.jurabeklabbackend.repository.ConstantsRepository;
import uz.mediasolutions.jurabeklabbackend.repository.ProductRepository;
import uz.mediasolutions.jurabeklabbackend.service.TransliteratorService;
import uz.mediasolutions.jurabeklabbackend.service.admin.abs.ProductService;
import uz.mediasolutions.jurabeklabbackend.service.common.impl.FileServiceImpl;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service("adminProductService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FileServiceImpl fileService;
    private final ConstantsRepository constantsRepository;
    private final TransliteratorService transliteratorService;

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
            throw RestException.restThrow(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<?> edit(Long id, ProductEditDTO dto) {
        Product product = productRepository.findByIdAndDeletedFalse(id).orElseThrow(
                () -> RestException.restThrow("Product not found", HttpStatus.NOT_FOUND)
        );
        String imageUrl = dto.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                fileService.deleteFile(product.getImageUrl());
            } catch (Exception e) {
                System.out.println("Error deleting image");
            }
            Optional.ofNullable(dto.getImageUrl()).ifPresent(product::setImageUrl);
            Optional.ofNullable(dto.getDescription()).ifPresent(product::setDescription);
            Optional.ofNullable(dto.getIsActive()).ifPresent(product::setActive);

            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Rest.EDITED);
        }
        throw RestException.restThrow("Image not found", HttpStatus.NOT_FOUND);
    }


    protected void saveDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new HSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            Set<String> processedProducts = new HashSet<>();
            List<Product> productsToSave = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() != 0) {
                    String name = getCellValue(row, 1);

                    if (!processedProducts.contains(name)) {
                        processedProducts.add(name);
                        Product product = processRow(row);
                        if (product != null) {
                            productsToSave.add(product);
                        }
                    }
                }
            }

            if (!productsToSave.isEmpty()) {
                productRepository.saveAll(productsToSave);
            }

        } catch (Exception e) {
            throw RestException.restThrow(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Har bir qatorni qayta ishlash
    private Product processRow(Row row) {
        String name = getCellValue(row, 1);
        String price = getCellValue(row, 4);
        String transliterate = transliteratorService.transliterate(name);

        // Narx bo'sh bo'lsa 0 ga o'zgartirish
        if (price.isEmpty()) {
            price = "0";
        }

        price = price.replaceAll(",", "");

        // Narxni ko'paytirish
        Constants constants = constantsRepository.findById(1L).orElseThrow(
                () -> RestException.restThrow("Constants not found", HttpStatus.NOT_FOUND)
        );

        float f = (float) constants.getProductPercent() / 100;
        BigDecimal newPrice = new BigDecimal(price).multiply(BigDecimal.valueOf(f + 1));

        if (name.isEmpty()) {
            return null;
        } else {
            Optional<Product> existingProductOpt = productRepository.findByNameAndDeletedFalse(name);
            if (existingProductOpt.isPresent()) {
                // Mahsulot mavjud bo'lsa, narxni yangilash
                Product existingProduct = existingProductOpt.get();
                existingProduct.setTranslate(transliterate);
                existingProduct.setPrice(newPrice);
                return existingProduct;
            } else {
                // Mahsulot mavjud bo'lmasa, yangi mahsulot yaratish
                return Product.builder()
                        .price(newPrice)
                        .name(name)
                        .translate(transliterate)
                        .deleted(false)
                        .isActive(true)
                        .build();
            }
        }
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString();
        }
        return "";
    }
}
