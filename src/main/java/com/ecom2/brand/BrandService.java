package com.ecom2.brand;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    List<Brand> getAllBrands();
    Brand findByName(String name);
    Brand addBrand(BrandDTO brandDTO) throws IOException;
    Brand editBrand(Long brandId, BrandDTO brandDTO) throws IOException;
    void deleteBrand(Long brandId);
}
