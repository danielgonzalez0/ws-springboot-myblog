package org.wildcodeschool.MyBlog.dto.category;

import jakarta.validation.constraints.Size;

public class CategoryCreateDTO {

@Size(min = 3, max = 50, message = "Le nom de la catégorie doit contenir entre 3 et 50 caractères")
    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
