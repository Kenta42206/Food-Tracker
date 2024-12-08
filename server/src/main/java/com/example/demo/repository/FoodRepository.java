package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Food;

public interface  FoodRepository extends JpaRepository<Food, Long> {
    /**
    * 食品名に指定されたキーワードを含む食品を検索し、ページネーションを適用して結果を返す。
    * 
    * @param name 検索する食品名のキーワード
    * @param pageable ページネーション情報（ページ番号とページサイズ）
    * @return {@code Page<Food>} ページネーションされた検索結果
    */
    @Query(value = "SELECT * FROM foods f WHERE f.name &@?1", nativeQuery = true)
    Page<Food> findByNameContaining(String name, Pageable pageable);

    /**
     * 指定されたIDの食品が存在するかどうかを確認する。
     * 
     * @param id 食品のID
     * @return {@code boolean} 指定されたIDの食品が存在する場合はtrue、それ以外はfalse
     */
    boolean existsById(long id);
}
