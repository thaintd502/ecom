package com.ecom2.brand;

import java.util.List;

public interface BrandService {
    public List<Brand> getAllBrands();
    public Brand findByName(String name);
}
