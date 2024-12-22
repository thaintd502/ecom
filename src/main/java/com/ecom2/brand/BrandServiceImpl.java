package com.ecom2.brand;

import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.exception.APIException;
import com.ecom2.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BrandServiceImpl implements BrandService{


    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findByName(String name) {
        return brandRepository.findByName(name);
    }

    @Override
    public Brand addBrand(BrandDTO brandDTO) throws IOException {
        if(brandRepository.existsByName(brandDTO.getName())){
            throw new APIException("Brand with the same name already exists!");
        }
        String imageUrl = "";
        if(brandDTO.getImageUrl() != null && !brandDTO.getImageUrl().isEmpty()){
            imageUrl = cloudinaryService.uploadFile(brandDTO.getImageUrl());
        }
        Brand brand = Brand.builder()
                .name(brandDTO.getName())
                .description(brandDTO.getDescription())
                .imageUrl(imageUrl)
                .build();
        brandRepository.save(brand);
        return brand;
    }

    @Override
    public Brand editBrand(Long brandId, BrandDTO brandDTO) throws IOException {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "brandId", brandId));
        String imageUrl = "";
        if(brandDTO.getImageUrl() != null && !brandDTO.getImageUrl().isEmpty()){
            imageUrl = cloudinaryService.uploadFile(brandDTO.getImageUrl());
            brand.setImageUrl(imageUrl);
        }
        brand.setName(brandDTO.getName());
        brand.setDescription(brandDTO.getDescription());
        brandRepository.save(brand);
        return brand;
    }

    @Override
    public void deleteBrand(Long brandId) {
        brandRepository.deleteById(brandId);
    }
}
