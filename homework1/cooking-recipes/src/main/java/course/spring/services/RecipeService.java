package course.spring.services;

import course.spring.models.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> getAllRecipes();
    Recipe getRecipeById(String id);
    Recipe addRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    Recipe deleteRecipe(String id);
}
