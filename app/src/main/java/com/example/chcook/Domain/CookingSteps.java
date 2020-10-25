package com.example.chcook.Domain;

public class CookingSteps {
    private String stepId;
    private String imageUrl;
    private String description;
    private Recipes recipes;

    public CookingSteps() {
    }

    public CookingSteps(String stepId, String imageUrl, String description, Recipes recipes) {
        this.stepId = stepId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.recipes = recipes;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Recipes getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipes recipes) {
        this.recipes = recipes;
    }
}
