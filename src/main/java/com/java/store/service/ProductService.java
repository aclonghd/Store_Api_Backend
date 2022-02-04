package com.java.store.service;

import com.java.store.dto.FileDto;
import com.java.store.module.Product;
import com.java.store.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void addProduct(Product product){
        productRepo.save(product);
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

    public void deleteProduct(Collection<Long> listProductId) throws Exception{
        if(productRepo.findAllById(listProductId).size() != listProductId.size()){
            throw new Exception("Bad Request");
        }
        productRepo.deleteAllById(listProductId);
    }

    public Set<String> uploadPhotosToProduct(List<FileDto> fileDtoList, Long productId) throws Exception {
        String[] mimeTypeValid = {"image/gif", "image/jpg" ,"image/jpeg", "image/png", "image/tiff"};
        if(!productRepo.existsById(productId)) throw new Exception("Product id is invalid");
        if(!gDriveCloudService.isExistFolder(productId.toString())) gDriveCloudService.createNewFolder(productId.toString());
        Product product = productRepo.getById(productId);
        for(FileDto fileDto : fileDtoList){
            if(Arrays.stream(mimeTypeValid).noneMatch(fileDto.getMimeType()::equalsIgnoreCase)) throw new Exception("File type is invalid");
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
            return true;
        }
        return false;
    }
}
