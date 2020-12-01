package course.spring.web;

import course.spring.exceptions.InvalidEntityDataException;
import course.spring.models.Recipe;
import course.spring.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

import java.util.List;

import static course.spring.utils.ErrorHandlingUtils.getViolationsAsStringList;

@RestController
@RequestMapping("/api/recipes")
public class RecipeResource {

    private RecipeService recipeService;

    @Autowired
    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable("id") String id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping
    public ResponseEntity<Recipe> createPost(@Valid @RequestBody Recipe recipe, Errors errors) {
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid post data", getViolationsAsStringList(errors));
        }
        Recipe created = recipeService.addRecipe(recipe);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().pathSegment("{id}")
                        .buildAndExpand(created.getId()).toUri()
        ).body(created);
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@PathVariable String id, @Valid @RequestBody Recipe recipe, Errors errors) {
        if(errors.hasErrors()){
            throw new InvalidEntityDataException("Invalid post data", getViolationsAsStringList(errors));
        }
        if(!id.equals(recipe.getId())) {
            throw new InvalidEntityDataException(
                    String.format("Post URL ID:%s differs from body entity ID:%s", id, recipe.getId()));
        }
        return recipeService.updateRecipe(recipe);
    }



    @DeleteMapping("/{id}")
    public Recipe deleteRecipe(@PathVariable String id) {
        return recipeService.deleteRecipe(id);
    }

}
