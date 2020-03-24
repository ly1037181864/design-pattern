package cn.topideal.com.redis;


import io.rebloom.client.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BloomTest {

    public static void main(String[] args) {
        BloomTest test = new BloomTest();
        List<String> users = test.randomUsers();
        List<String> usersTrain = users.subList(0, users.size() / 2);
        List<String> usersTest = users.subList(0, users.size() / 2);

        Client client = new Client("127.0.0.1", 6379);
        client.delete("codehold");
        client.createFilter("codehold", 50000, 0.001);
        for (String user : usersTrain) {
            client.add("codehold", user);
        }

        int falses = 0;
        for (String user : usersTest) {
            boolean ret = client.exists("codehold", user);
            if (ret)
                falses++;
        }
        System.out.printf("%d % d\n", falses, usersTest.size());
        client.close();

    }

    private String chars;

    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            builder.append((char) ('a' + i));
        }
        chars = builder.toString();
    }

    private String randomString(int n) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int idx = ThreadLocalRandom.current().nextInt(chars.length());
            builder.append(chars.charAt(idx));
        }

        return builder.toString();
    }

    private List<String> randomUsers() {
        List<String> users = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            users.add(randomString(64));
        }
        return users;
    }


}
