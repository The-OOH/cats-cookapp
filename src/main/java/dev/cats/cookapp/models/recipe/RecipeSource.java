package dev.cats.cookapp.models.recipe;

public enum RecipeSource {
    AI, // chatbot generated recipe
    MANUALLY_CREATED, // user created recipe
    EXTERNAL, // e.g. from a recipe website(klopotenko website parser)
    ORIGINAL // kitchen stories dump
}
