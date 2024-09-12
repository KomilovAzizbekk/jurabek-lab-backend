package uz.mediasolutions.jurabeklabbackend.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service("adminProductService")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FileServiceImpl fileService;

    // Parallel qayta ishlash uchun oqimlar sonini belgilash (4 ta oqim)
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public ResponseEntity<?> getAll(int page, int size, String search) {
        Page<ProductDTO> allProducts = productRepository.findAllWithSearch(search, PageRequest.of(page, size));
        return ResponseEntity.ok(allProducts);
    }

    @Override
//    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
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

//    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    protected void saveDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new HSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);

            // Parallel ishlash uchun natijalarni yig'ish
            List<Future<List<Product>>> futures = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() != 0) {
                    // Har bir qatorni parallel ravishda qayta ishlaymiz
                    Future<List<Product>> future = executorService.submit(() -> processRow(row));
                    futures.add(future);
                }
            }

            // Barcha natijalarni parallel jarayondan yig'ib olish
            List<Product> allProducts = new ArrayList<>();
            for (Future<List<Product>> future : futures) {
                allProducts.addAll(future.get());
            }

            try {
                // Batch Insert yordamida barcha mahsulotlarni saqlash
                productRepository.saveAll(allProducts);
            } catch (DataIntegrityViolationException e) {
                System.out.println("Duplicate entry found for one of the products");
            }
        } catch (Exception e) {
            throw RestException.restThrow(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Har bir qatorni qayta ishlash
    private List<Product> processRow(Row row) {
        List<Product> products = new ArrayList<>();

        String name = getCellValue(row, 1);
        String price = getCellValue(row, 4);

        // Narx bo'sh bo'lsa 0 ga o'zgartirish
        if (price.isEmpty()) {
            price = "0";
        }

        price = price.replaceAll(",", "");

        // Narxni ko'paytirish
        BigDecimal newPrice = new BigDecimal(price).multiply(new BigDecimal("1.2"));

        if (!name.isEmpty()) {
            Optional<Product> existingProductOpt = productRepository.findByNameAndDeletedFalse(name);
            if (existingProductOpt.isPresent()) {
                // Mahsulot mavjud bo'lsa, narxni yangilash
                Product existingProduct = existingProductOpt.get();
                existingProduct.setPrice(newPrice);
                products.add(existingProduct);
            } else {
                // Mahsulot mavjud bo'lmasa, yangi mahsulot yaratish
                Product product = Product.builder()
                        .price(newPrice)
                        .name(name)
                        .deleted(false)
                        .build();
                products.add(product);
            }
        }
        return products;
    }

    private String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell != null) {
            return cell.toString();
        }
        return "";
    }
}
