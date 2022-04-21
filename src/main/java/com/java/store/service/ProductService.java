package com.java.store.service;

import com.java.store.dto.ProductDto;
import com.java.store.dto.ProductInfoDto;
import com.java.store.mapper.ProductMapper;
import com.java.store.module.Product;
import com.java.store.module.Tags;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.TagRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final GDriveCloudService gDriveCloudService;
    private final TagRepo tagRepo;

    public ProductDto getProduct(Long productId) throws Exception{
        if(productRepo.existsById(productId)){
            Product product =  productRepo.getById(productId);
            return productMapper.EntityToDto(product);
        }
        throw new Exception("Bad Request");
    }

    public ProductDto getProductByTitleUrl(String titleUrl) throws Exception{
        if(productRepo.existsByTitleUrl(titleUrl)){
        Product product = productRepo.getByTitleUrl(titleUrl);
        return productMapper.EntityToDto(product);
        } throw new Exception("Page not found");
    }

    public List<String> getAllProductTag(){
        return productRepo.getAllProductTag();
    }

    public List<ProductInfoDto> findAllByProductTag(String productTag){
        List<ProductInfoDto> res = new ArrayList<>();
        List<Product> productList = productRepo.findAllByProductTag(productTag);
        for(Product product : productList){
            ProductInfoDto productInfoDto = productMapper.EntityToInfoDto(product);
            res.add(productInfoDto);
        }
        return res;
    }

    public Long addProduct(ProductDto newProduct) throws Exception {
        Product product = productMapper.DtoToEntity(newProduct);
        product.setImageUrl(new HashSet<>());
        product.setVoteNumber(0);
        product.setAverageRatting(0);
        Set<Tags> tags = new HashSet<>();
        newProduct.getTags().forEach(tagTitle ->{
            Tags tag = new Tags();
            if(!tagRepo.existsByTitle(tagTitle)){

                tag.setTitle(tagTitle);
                tagRepo.save(tag);
                tagRepo.flush();

            } else {
                tag = tagRepo.getByTitle(tagTitle);
            }
            tags.add(tag);
        });
        product.setTags(tags);
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

    public void updateProduct(ProductDto productDto) throws Exception{
        if(!productRepo.existsById(productDto.getId())){
            throw new Exception("Bad Request");
        }
        Product product = productMapper.DtoToEntity(productDto);
        product.setId(productDto.getId());
        Set<Tags> tags = new HashSet<>();
        productDto.getTags().forEach(tagTitle ->{
            Tags tag = new Tags();
            if(!tagRepo.existsByTitle(tagTitle)){

                tag.setTitle(tagTitle);
                tagRepo.save(tag);
                tagRepo.flush();

            } else {
                tag = tagRepo.getByTitle(tagTitle);
            }
            tags.add(tag);
        });
        product.setTags(tags);
        if(product.getMainImage() == null && !product.getImageUrl().isEmpty()) product.setMainImage(product.getImageUrl().stream().findFirst().get());
        productRepo.save(product);
    }

    public void deleteProduct(Long productId) throws Exception{
        if(productRepo.findById(productId).isEmpty()){
            throw new Exception("Bad Request");
        }
        Product product = productRepo.getById(productId);
        if(product.getImageUrl().size() > 0){
            gDriveCloudService.deleteFolder(String.valueOf(productId));
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
        if(product.getMainImage() == null){
            product.setMainImage(product.getImageUrl().stream().findFirst().get());
        }
        productRepo.save(product);
        return product.getImageUrl();
    }

    public void removePhotoFromProduct(String photoId, Long productId) throws Exception{
        if(!productRepo.existsById(productId)) throw new Exception("Product id is invalid");
        Product product = productRepo.getById(productId);
        gDriveCloudService.deleteFile(photoId);
        product.getImageUrl().remove(String.format("https://drive.google.com/uc?export=view&id=%s", photoId));
        if(product.getMainImage().equals(String.format("https://drive.google.com/uc?export=view&id=%s", photoId))) {
            if(product.getImageUrl().stream().findFirst().isPresent())
                product.setMainImage(product.getImageUrl().stream().findFirst().get());
            else
                product.setMainImage(null);
        }
        productRepo.save(product);
    }
}
