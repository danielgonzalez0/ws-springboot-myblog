package org.wildcodeschool.MyBlog.dto.author;

import jakarta.validation.constraints.Size;

public class AuthorCreateDTO {

    @Size(min = 3, max = 50, message = "Le prénom de l'auteur doit contenir entre 3 et 50 caractères")
    private String firstName;
    @Size(min = 3, max = 50, message = "Le nom de l'auteur doit contenir entre 3 et 50 caractères")
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
