package course.spring.services.impl;

import course.spring.dao.RecipeRepository;
import course.spring.exceptions.ForbiddenAccessEntityException;
import course.spring.exceptions.NonExistingEntityException;
import course.spring.models.Recipe;
import course.spring.models.users.Role;
import course.spring.models.users.User;
import course.spring.services.RecipeService;
import course.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static course.spring.models.users.Role.USER;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;
    private UserService userService;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, UserService userService) {
        this.recipeRepository = recipeRepository;
        this.userService = userService;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Recipe getRecipeById(String id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() ->
                new NonExistingEntityException(String.format("Recipe with ID:%s does not exist.", id)));
        checkForPermission(recipe.getUserId());
        return recipe;
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {
        recipe.setId(null);
        recipe.setUserId(getAuthenticatedUser().getId());
        return recipeRepository.insert(recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        checkForPermission(recipe.getUserId());
        getRecipeById(recipe.getId());
        recipe.setUpdatedAt(LocalDateTime.now());
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe deleteRecipe(String id) {
        Recipe removed = getRecipeById(id);
        checkForPermission(removed.getUserId());
        recipeRepository.deleteById(id);
        return removed;
    }

    private User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUserByUsername(username);
    }

    private void checkForPermission(String userId) {
        User author = getAuthenticatedUser();
        if(!userId.equals(author.getId()) && author.getRole() == USER) {
            throw new ForbiddenAccessEntityException(String.format("User with ID:%s can`t access this recipe.", author.getId()));
        }
    }
}
