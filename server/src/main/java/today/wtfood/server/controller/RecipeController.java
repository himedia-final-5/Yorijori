package today.wtfood.server.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import today.wtfood.server.dto.GeneratedId;
import today.wtfood.server.dto.PageResponse;
import today.wtfood.server.dto.recipe.RecipeDetail;
import today.wtfood.server.dto.recipe.RecipeDto;
import today.wtfood.server.dto.recipe.RecipeSummary;
import today.wtfood.server.entity.Recipe;
import today.wtfood.server.repository.CommentRepository;
import today.wtfood.server.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService rs;

    public RecipeController(RecipeService rs) {
        this.rs = rs;
    }

    // 레시피 리스트 (페이지네이션)
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public PageResponse<RecipeSummary> getRecipeList(@RequestParam("category") String category, Pageable pageable) {
        if (category == null || category.isEmpty()) {
            return PageResponse.of(rs.getRecipeList(pageable));
        } else {
            return PageResponse.of(rs.getRecipesByCategory(category, pageable));
        }
    }

    // 레시피 리스트 //레시피번호(id)
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public RecipeDetail getRecipeById(@PathVariable("id") long id) {
        return rs.getRecipeById(id);
    }

    // 제목, 카테고리, 설명, 해시태그로 레시피 검색
    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public PageResponse<RecipeSummary> searchRecipes(
            @RequestParam(value = "term", required = false) String term,
            Pageable pageable
    ) {
        return PageResponse.of(rs.searchRecipes(term, pageable));
    }

    //조회수
    @PutMapping("{id}/incrementViewCount")
    public void incrementViewCount(@PathVariable Long id) {
        rs.incrementViewCount(id);
    }

    // 레시피 수정
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRecipe(
            @PathVariable("id") long id,
            @RequestBody RecipeDto recipe
    ) {
        Recipe updatedRecipe = recipe.toEntity();
        rs.updateRecipe(id, updatedRecipe);
    }

    // 레시피 삭제
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") long id) {
        rs.deleteRecipe(id);
    }

    // 새로운 레시피 생성
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GeneratedId<Long> createRecipe(@RequestBody RecipeDto recipeDto) {
        return GeneratedId.of(rs.createRecipe(recipeDto).getId());
    }

    // 레시피 찜하기
    @PostMapping("/{recipeId}/favorite")
    @PreAuthorize("isAuthenticated()")
    public void addFavoriteRecipe(@RequestParam long memberId, @PathVariable long recipeId) {
        rs.addFavoriteRecipe(memberId, recipeId);
    }

    // 찜한 레시피 목록 조회 (페이지네이션 추가)
    @GetMapping("/favorites")
    @PreAuthorize("isAuthenticated()")
    public PageResponse<RecipeSummary> getFavoriteRecipes(
            @RequestParam long memberId,
            Pageable pageable
    ) {
        return PageResponse.of(rs.getFavoriteRecipes(memberId, pageable));
    }

    // 찜하기 취소
    @DeleteMapping("/{recipeId}/favorite")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriteRecipe(@RequestParam long memberId, @PathVariable long recipeId) {
        rs.deleteFavoriteRecipe(memberId, recipeId);
    }

    // 댓글 가져오기
    @GetMapping("/{recipeId}/comments")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Recipe.Comment>> getComments(@PathVariable long recipeId) {
        List<Recipe.Comment> comments = rs.getComments(recipeId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 추가
    @PostMapping("/{recipeId}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addComment(
            @PathVariable long recipeId,
            @RequestParam long memberId,
            @RequestBody Recipe.Comment comment) {
        comment.setRecipe(rs.getRecipeByIdEntity(recipeId));
        comment.setMember(rs.getMemberById(memberId));
        rs.insertComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment added successfully");
    }

    // 댓글 수정
    @PutMapping("/{recipeId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated() and @securityService.isCommentOwner(#commentId, #memberId)")
    public ResponseEntity<String> updateComment(
            @PathVariable long recipeId,
            @PathVariable long commentId,
            @RequestParam long memberId,
            @RequestBody Recipe.Comment updatedComment) {
        rs.updateComment(commentId, updatedComment);
        return ResponseEntity.ok("Comment updated successfully");
    }

    // 댓글 삭제
    @DeleteMapping("/{recipeId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated() and @securityService.isCommentOwner(#commentId, #memberId)")
    public ResponseEntity<String> deleteComment(
            @PathVariable long recipeId,
            @PathVariable long commentId,
            @RequestParam long memberId) {
        rs.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully");
    }

    @Service
    public class SecurityService {

        private final CommentRepository commentRepository;

        public SecurityService(CommentRepository commentRepository) {
            this.commentRepository = commentRepository;
        }

        public boolean isCommentOwner(long commentId, long memberId) {
            Recipe.Comment comment = commentRepository.findById(commentId).orElse(null);
            return comment != null && comment.getMember().getId() == memberId;
        }
    }

}
