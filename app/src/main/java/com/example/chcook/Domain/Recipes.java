package com.example.chcook.Domain;

public class Recipes {
    private String recipeId="";
    private String title="";
    private String description="";
    private String imageUrl="";
    private String uploadDate="";

    public Recipes() {
    }

    public Recipes(String recipeId) {
        this.recipeId = recipeId;
    }

    public Recipes(String recipeId, String title, String description, String imageUrl, String uploadDate) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.uploadDate = uploadDate;
    }

    public Recipes(String recipeId, String title, String imageUrl, String uploadDate) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.uploadDate = uploadDate;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        uploadDate = uploadDate;
    }
}
