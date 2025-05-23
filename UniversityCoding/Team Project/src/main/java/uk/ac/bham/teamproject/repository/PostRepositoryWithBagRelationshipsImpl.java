package uk.ac.bham.teamproject.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.ac.bham.teamproject.domain.Post;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PostRepositoryWithBagRelationshipsImpl implements PostRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Post> fetchBagRelationships(Optional<Post> post) {
        return post.map(this::fetchDietaryTags);
    }

    @Override
    public Page<Post> fetchBagRelationships(Page<Post> posts) {
        return new PageImpl<>(fetchBagRelationships(posts.getContent()), posts.getPageable(), posts.getTotalElements());
    }

    @Override
    public List<Post> fetchBagRelationships(List<Post> posts) {
        return Optional.of(posts).map(this::fetchDietaryTags).orElse(Collections.emptyList());
    }

    Post fetchDietaryTags(Post result) {
        return entityManager
            .createQuery("select post from Post post left join fetch post.dietaryTags where post is :post", Post.class)
            .setParameter("post", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Post> fetchDietaryTags(List<Post> posts) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, posts.size()).forEach(index -> order.put(posts.get(index).getId(), index));
        List<Post> result = entityManager
            .createQuery("select distinct post from Post post left join fetch post.dietaryTags where post in :posts", Post.class)
            .setParameter("posts", posts)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
