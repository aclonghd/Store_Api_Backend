package com.java.store.service;

import com.java.store.dto.ProductDto;
import com.java.store.exception.ServiceException;
import com.java.store.mapper.ProductMapper;
import com.java.store.module.Product;
import com.java.store.module.Tags;
import com.java.store.repository.ProductRepo;
import com.java.store.repository.TagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.http.HttpStatus.*;

import java.util.*;

@Service
public class ProductService {
    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final GDriveCloudService gDriveCloudService;
    private final TagRepo tagRepo;

    @Autowired
    public ProductService(ProductRepo productRepo, ProductMapper productMapper, GDriveCloudService gDriveCloudService, TagRepo tagRepo) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
        this.gDriveCloudService = gDriveCloudService;
        this.tagRepo = tagRepo;
    }

    public ProductDto getProduct(Long productId) {
        if(productRepo.existsById(productId)){
            Product product =  productRepo.getById(productId);
            return productMapper.EntityToDto(product);
        }
        throw new ServiceException(BAD_REQUEST.value(), "Product not found!");
    }

    public ProductDto getProductByTitleUrl(String titleUrl){
        if(productRepo.existsByTitleUrl(titleUrl)){
        Product product = productRepo.getByTitleUrl(titleUrl);
        return productMapper.EntityToDto(product);
        } throw new ServiceException(BAD_REQUEST.value(),"Page not found");
    }

    public List<String> getAllProductTag(){
        return productRepo.getAllProductTag();
    }

    public List<ProductDto> findAllByProductTag(String productTag){
        List<ProductDto> res = new ArrayList<>();
        List<Product> productList = productRepo.findAllByProductTag(productTag);
        for(Product product : productList){
            ProductDto productInfoDto = productMapper.EntityToDto(product);
            res.add(productInfoDto);
        }
        return res;
    }

    public Long addProduct(ProductDto newProduct, Set<MultipartFile> files) {
        Product product = productMapper.DtoToEntity(newProduct);
        product.setImageUrl(new HashSet<>());
        product.setVoteNumber(0);
        product.setTotalRatingScore(0);
        Set<Tags> tags = new HashSet<>();
        if(newProduct.getTags() != null) {
            for (String tagTitle : newProduct.getTags()) {
                Tags tag = new Tags();
                if (!tagTitle.equals("")) {
                    if (!tagRepo.existsByTitle(tagTitle)) {
                        tag.setTitle(tagTitle);
                        tagRepo.save(tag);
                        tagRepo.flush();
                    } else {
                        tag = tagRepo.getByTitle(tagTitle);
                    }
                    tags.add(tag);
                }
            }
        }
        product.setTags(tags);
        productRepo.save(product);
        productRepo.flush();
        if(files != null)
            uploadPhotosToProduct(new ArrayList<>(files), product.getId());
        return product.getId();
    }

    public List<ProductDto> getAllProduct(){
        List<ProductDto> res = new ArrayList<>();
        List<Product> productList = productRepo.findAll();
        for(Product product : productList){
            ProductDto productInfoDto = productMapper.EntityToDto(product);
            res.add(productInfoDto);
        }
        return res;
    }

    public void updateProduct(ProductDto productDto){
        if(!productRepo.existsById(productDto.getId())){
            throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        }
        Product product = productMapper.DtoToEntity(productDto);
        product.setId(productDto.getId());
        Set<Tags> tags = new HashSet<>();
        if(productDto.getTags() != null) {
            productDto.getTags().forEach(tagTitle -> {
                Tags tag = new Tags();
                if (!tagTitle.equals("")) {
                    if (!tagRepo.existsByTitle(tagTitle)) {

                        tag.setTitle(tagTitle);
                        tagRepo.save(tag);
                        tagRepo.flush();

                    } else {
                        tag = tagRepo.getByTitle(tagTitle);
                    }
                    tags.add(tag);
                }
            });
        }
        product.setTags(tags);
        if((product.getMainImage() == null || product.getMainImage().equals("null")) && !product.getImageUrl().isEmpty()) product.setMainImage(product.getImageUrl().stream().findFirst().get());
        productRepo.save(product);
    }

    public void deleteProduct(Long productId){
        if(productRepo.findById(productId).isEmpty()){
            throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        }
        Product product = productRepo.getById(productId);
        if(product.getImageUrl().size() > 0){
            gDriveCloudService.deleteFolder(String.valueOf(productId));
        }
        productRepo.deleteById(productId);
    }

    public Set<String> uploadPhotosToProduct(List<MultipartFile> fileDtoList, Long productId) {
        String[] mimeTypeValid = {"image/gif", "image/jpg" ,"image/jpeg", "image/png", "image/tiff"};
        if(!productRepo.existsById(productId)) throw new ServiceException(BAD_REQUEST.value(),"Product id is invalid");
        try {
            if(!gDriveCloudService.isExistFolder(productId.toString())) gDriveCloudService.createNewFolder(productId.toString());
        } catch (Exception e) {
            throw new ServiceException(BAD_REQUEST.value(),e.getMessage());
        }
        Product product = productRepo.getById(productId);
        for(MultipartFile fileDto : fileDtoList){
            if(Arrays.stream(mimeTypeValid).noneMatch(Objects.requireNonNull(fileDto.getContentType())::equalsIgnoreCase)) throw new ServiceException(BAD_REQUEST.value(),"File type is invalid");
            String photoId = gDriveCloudService.uploadFile(fileDto, productId.toString());
            String urlImage = String.format("https://drive.google.com/uc?id=%s", photoId);
            product.getImageUrl().add(urlImage);
        }
        if(product.getMainImage() == null){
            product.setMainImage(product.getImageUrl().stream().findFirst().get());
        }
        productRepo.save(product);
        return product.getImageUrl();
    }

    public void removePhotoFromProduct(String photoId, Long productId) {
        if(!productRepo.existsById(productId)) throw new ServiceException(BAD_REQUEST.value(),"Product id is invalid");
        Product product = productRepo.getById(productId);
        gDriveCloudService.deleteFile(photoId);
        product.getImageUrl().remove(String.format("https://drive.google.com/uc?id=%s", photoId));
        if(product.getMainImage().equals(String.format("https://drive.google.com/uc?id=%s", photoId))) {
            if(product.getImageUrl().stream().findFirst().isPresent())
                product.setMainImage(product.getImageUrl().stream().findFirst().get());
            else
                product.setMainImage(null);
        }
        productRepo.save(product);
    }
}
