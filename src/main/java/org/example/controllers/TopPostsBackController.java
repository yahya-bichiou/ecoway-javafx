package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.example.services.postService;

import java.sql.SQLException;
import java.util.List;

public class TopPostsBackController {

    @FXML
    private BarChart<String, Number> barChart;

    private final postService postService;

    public TopPostsBackController() throws SQLException {
        this.postService = new postService();
    }

    @FXML
    public void initialize() {
        loadTopPosts();
    }

    private void loadTopPosts() {
        try {
            List<Object[]> topPosts = postService.getTop5PostsByComments();
            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for (Object[] obj : topPosts) {
                String titre = (String) obj[0];
                int nbCommentaires = (Integer) obj[1];
                series.getData().add(new XYChart.Data<>(titre, nbCommentaires));
            }

            barChart.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
