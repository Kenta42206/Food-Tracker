package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Food;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FoodRepository;

@Service
public class FoodService {
    @Autowired
    FoodRepository foodRepository;
    
    /**
     * 名前で食べ物を検索し、ページネーションを適用して結果を返す。
     * 食品名が空またはnullの場合は例外がスローされる。
     * 
     * @param name 検索する食品名のキーワード
     * @param page 取得するページ番号
     * @param size 1ページあたりのアイテム数
     * @return {@code Map<String, Object>} ページネーションされた食品リストと関連情報
     * @throws IllegalArgumentException 食品名が空またはnullの場合
     */
    public Map<String, Object> getFoodsByNameKeywordWithPage(String name, int page, int size){
        List<Food> foods = new ArrayList<>();
        Pageable paging = PageRequest.of(page, size);
        Page<Food> pageTuts;

        if(name == null) {
            throw new IllegalArgumentException("Food name must not be empty or null");
        } else {
            pageTuts = foodRepository.findByNameContaining(name, paging);
        }

        foods = pageTuts.getContent();
        Map<String, Object> response = new HashMap<>();

        response.put("foods", foods);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());

        return response;    
    }

    /**
     * 食品IDを指定して、食品情報を取得する。
     * 食品IDに該当するレコードが見つからない場合は、例外がスローされる。
     * 
     * @param id 取得する食品のID
     * @return {@code Food} 取得された食品情報
     * @throws ResourceNotFoundException 食品IDに該当する食品が見つからなかった場合
     */
    public Food getFoodById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException("Food not found with id {" + id + "}");
        });

        return food;
    }
}
