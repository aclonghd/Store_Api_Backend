package com.java.store.service;

import com.java.store.dto.NewProductDto;
import com.java.store.module.Product;
import com.java.store.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final GDriveCloudService gDriveCloudService;

    public Product getProduct(Long productId) throws Exception{
        if(productRepo.existsById(productId)){
            return productRepo.getById(productId);
        }
        throw new Exception("Bad Request");
    }

    public Long addProduct(NewProductDto newProduct) throws Exception {
        long id = (long) productRepo.findAll().size() + 1;
        Product product = new Product();
        product.setId(id);
        product.setTitle(newProduct.getTitle());
        product.setPrice(newProduct.getPrice());
        product.setColor(newProduct.getColor());
        product.setInformation(newProduct.getInformation());
        product.setQuantity(newProduct.getQuantity());
        product.setImageUrl(new HashSet<>());
        productRepo.save(product);
        if(newProduct.getFiles() != null)
            uploadPhotosToProduct(new ArrayList<>(newProduct.getFiles()), id);
        return id;
    }

    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }

    public void updateProduct(Product product) throws Exception{
        if(!productRepo.existsById(product.getId())){
            throw new Exception("Bad Request");
        }
        productRepo.save(product);
    }

    public void deleteProduct(Long productId) throws Exception{
        if(productRepo.findById(productId).isEmpty()){
            throw new Exception("Bad Request");
        }
        Product product = productRepo.getById(productId);
        if(product.getImageUrl().size() > 0){
            if(!gDriveCloudService.deleteFolder(String.valueOf(productId))) throw new Exception("FolderNotFound");
        }
        productRepo.deleteById(productId);
    }

    public Set<String> uploadPhotosToProduct(List<MultipartFile> fileDtoList, Long productId) throws Exception {
        String[] mimeTypeValid = {"image/gif", "image/jpg" ,"image/jpeg", "image/png", "image/tiff"};
        if(!productRepo.existsById(productId)) throw new Exception("Product id is invalid");
        if(!gDriveCloudService.isExistFolder(productId.toString())) gDriveCloudService.createNewFolder(productId.toString());
        Product product = productRepo.getById(productId);
        for(MultipartFile fileDto : fileDtoList){
            if(Arrays.stream(mimeTypeValid).noneMatch(Objects.requireNonNull(fileDto.getContentType())::equalsIgnoreCase)) throw new Exception("File type is invalid");
            String photoId = gDriveCloudService.uploadFile(fileDto, productId.toString());
            String urlImage = String.format("https://drive.google.com/uc?export=view&id=%s", photoId);
            product.getImageUrl().add(urlImage);
        }
        productRepo.save(product);
        return product.getImageUrl();
    }

    public boolean removePhotoFromProduct(String photoId, Long productId) throws Exception{
        if(!productRepo.existsById(productId)) throw new Exception("Product id is invalid");
        Product product = productRepo.getById(productId);
        if(gDriveCloudService.deleteFile(photoId)){
            product.getImageUrl().remove(String.format("https://drive.google.com/uc?export=view&id=%s", photoId));
            productRepo.save(product);
            return true;
        }
        return false;
    }
}
