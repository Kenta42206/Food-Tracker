package com.example.demo.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.service.MealhistoryService;

@Component
public class MealhistoryScheduledTasks {
    @Autowired
    MealhistoryService mealhistoryService;

    private static final Logger logger = LoggerFactory.getLogger(MealhistoryService.class);

    /**
     * 毎日深夜0時に実行されるタスクで、`deleteFlg` が `true` の食事履歴データを削除する。
     * 食事履歴サービスを使用して、削除フラグが立っている食事履歴を削除し、その結果をログに記録する。
     * 削除処理中にエラーが発生した場合は、エラーログが記録される。
     * 
     * @see mealhistoryService#deleteMealhistoriesWithDeleteFlg()
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Tokyo")
    public void deleteMealhistoriesTask(){

        logger.info("Start deleting meal histories with deleteFlg = true");

        try {
            mealhistoryService.deleteMealhistoriesWithDeleteFlg();
            logger.info("Finished deleting meal histories with deleteFlg = true");
        } catch (Exception e) {
            logger.error("Failed to delete meal histories with deleteFlg = true", e);
        }
    }
}
