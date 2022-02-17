package recipes.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import recipes.models.IdValue;
import recipes.models.Recipe;
import recipes.models.User;
import recipes.repository.RecipeRepository;
import recipes.services.UserDetailsImpl;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
public class RecipesController {

    @Autowired
    RecipeRepository recipeRepository;

    @GetMapping("/api/recipe/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        var recipe = recipeRepository.findById(id);
        return recipe == null ? new ResponseEntity<>(null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(recipe, HttpStatus.OK);

    }

    @GetMapping(value = "/api/recipe/search", params = "name")
    public ResponseEntity<List<Recipe>> searchRecipeByName(@RequestParam(required = false) String name,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        if (name == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        List<Recipe> recipeList;

        recipeList = recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
        System.out.println(Arrays.toString(recipeList.toArray()));

        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    @GetMapping(value = "/api/recipe/search", params = "category")
    public ResponseEntity<List<Recipe>> searchRecipeByCategory(@RequestParam(required = false) String category,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        if (category == null) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        List<Recipe> recipeList;

        recipeList = recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
        return new ResponseEntity<>(recipeList, HttpStatus.OK);
    }

    @GetMapping("/api/recipe/get_all")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @PutMapping("/api/recipe/{id}")
    public ResponseEntity updateRecipe(@PathVariable long id,
                                       @Valid @RequestBody Recipe updateRecipe,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        Recipe recipe = recipeRepository.findById(id);

        if (recipe != null && !recipe.getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if (recipe == null) return new ResponseEntity(HttpStatus.NOT_FOUND);

        updateRecipe.setId(recipe.getId());
        recipeRepository.save(updateRecipe);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/recipe/new")
    public ResponseEntity<IdValue> postRecipe(@Valid @RequestBody Recipe recipe, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        recipe.setAuthor(userDetails.getUsername());
        recipeRepository.save(recipe);
        return new ResponseEntity<>(new IdValue(recipe.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity deleteRecipe(@PathVariable long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null)
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        if (!recipeRepository.existsById(id))
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        var author = recipeRepository.findById(id).getAuthor();
        if (author == null) {
            recipeRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        if (!recipeRepository.findById(id).getAuthor().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        recipeRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
