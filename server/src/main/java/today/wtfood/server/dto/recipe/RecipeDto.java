package today.wtfood.server.dto.recipe;

import lombok.Value;
import today.wtfood.server.entity.Recipe;

import java.io.Serializable;
import java.util.List;


/**
 * DTO for {@link today.wtfood.server.entity.Event}
 */

//수정생성용 dto
@Value
public class RecipeDto implements Serializable {
    String bannerImage;
    String title;
    String description;
    String cookingTime;
    Integer servings;
    Integer level;
    String videoLink;
    String category;
    List<String> ingredientImage;
    List<String> ingredients;
    List<String> cookingTools;
    List<String> guideLinks;
    List<Recipe.CookingStep> cookingStep;
    List<String> finishedImages;

    public Recipe toEntity() {
        Recipe recipe = new Recipe();
        recipe.setBannerImage(bannerImage);
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setCookingTime(cookingTime);
        recipe.setServings(servings);
        recipe.setLevel(level);
        recipe.setVideoLink(videoLink);
        recipe.setCategory(category);
        recipe.setIngredientImage(ingredientImage);
        recipe.setIngredients(ingredients);
        recipe.setCookingTools(cookingTools);
        recipe.setGuideLinks(guideLinks);
        recipe.setCookingStep(cookingStep);
        recipe.setFinishedImages(finishedImages);
        return recipe;
    }


}
