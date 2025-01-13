package com.example.githubapi_test;

import okhttp3.*;

import java.io.IOException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



public class GitHubApiTest {

    private static final String GITHUB_TOKEN = "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";
    private static final String GITHUB_BASE_URL = "https://api.github.com";


    public static void main(String[] args) {
        String owner = "ysoup-lab";
        String repo = "github_api_test";
        int issueNumber = 1;  // 替换为实际 Issue 编号
        String label = "enhancement";

        try {
            addLabelToIssue(owner, repo, issueNumber, label);
//            getRepositoryInfo(owner, repo);
//            getPullRequestInfo(owner, repo, issueNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addLabelToIssue(String owner, String repo, int issueNumber, String label) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("%s/repos/%s/%s/issues/%d/labels", GITHUB_BASE_URL, owner, repo, issueNumber);

        String jsonBody = String.format("{\"labels\": [\"%s\"]}", label);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Label added successfully: " + response.body().string());
            } else {
                System.err.println("Failed to add label: " + response.body().string());
            }
        }
    }

    public static void getRepositoryInfo(String owner, String repo) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("%s/repos/%s/%s", GITHUB_BASE_URL, owner, repo);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                System.out.println("Repository Info: " + response.body().string());
            } else {
                System.err.println("Failed to get repository info: " + response.body().string());
            }
        }
    }

    // 获取 PR 信息
    public static void getPullRequestInfo(String owner, String repo, int prNumber) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String url = String.format("%s/repos/%s/%s/pulls/%d", GITHUB_BASE_URL, owner, repo, prNumber);

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + GITHUB_TOKEN)
                .header("Accept", "application/vnd.github.v3+json")
                .get()
                .build();



        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // 打印 PR 信息
                String responseBody = response.body().string();

                System.out.println("PR Info: " + responseBody);

                // 使用 fastjson 解析 JSON 数据
                JSONObject jsonObject = JSON.parseObject(responseBody);

                // 检查 merged 状态
                if (jsonObject.getBoolean("merged") != null && jsonObject.getBoolean("merged")) {
                    System.out.println("The PR has been merged.");
                } else {
                    System.out.println("The PR has NOT been merged.");
                }
            } else {
                System.err.println("Failed to get PR info: " + response.body().string());
            }
        }
    }

}