package com.java.store.service;

import com.java.store.dto.NewProductDto;
import com.java.store.dto.ProductInfoDto;
import com.java.store.mapper.ProductMapper;
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
    private final ProductMapper productMapper;
    private final GDriveCloudService gDriveCloudService;

    public Product getProduct(Long productId) throws Exception{
        if(productRepo.existsById(productId)){
            return productRepo.getById(productId);
        }
        throw new Exception("Bad Request");
    }

    public Long addProduct(NewProductDto newProduct) throws Exception {
        Product product = new Product();
        product.setTitle(newProduct.getTitle());
        product.setPrice(newProduct.getPrice());
        product.setColor(newProduct.getColor());
        product.setInformation(newProduct.getInformation());
        product.setQuantity(newProduct.getQuantity());
        product.setImageUrl(new HashSet<>());
        product.setVoteNumber(0);
        product.setAverageRatting(0);
        productRepo.save(product);
        productRepo.flush();
        if(newProduct.getFiles() != null)
            uploadPhotosToProduct(new ArrayList<>(newProduct.getFiles()), product.getId());
        return product.getId();
    }

    public List<ProductInfoDto> getAllProduct(){
        List<ProductInfoDto> res = new ArrayList<>();
        List<Product> productList = productRepo.findAll();
        for(Product product : productList){
            ProductInfoDto productInfoDto = productMapper.EntityToInfoDto(product);
            res.add(productInfoDto);
        }
        return res;
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
        if(product.getImageUrl().stream().findFirst().isPresent())
            product.setMainImage(product.getImageUrl().stream().findFirst().get());
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
