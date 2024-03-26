package DFS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Graph {
    public static void main(String[] args) {
        Map<String, List<String>> danhSach = readFile("src/DFS/input.txt");
        if (danhSach != null) {
            String res = dfs(danhSach, "A", "G");
            if (res != null) {
                printFile("src/DFS/output.txt", res);
                System.out.println(res);
            } else {
                System.out.println("Không tìm thấy đường đi từ A đến G.");
            }
        } else {
            System.out.println("Đã xảy ra lỗi khi đọc file input.txt.");
        }
    }
  
    public static String dfs(Map<String, List<String>> danhSach, String start, String end) {
        if (!danhSach.containsKey(start) || !danhSach.containsKey(end)) {
            return null;
        }

        Stack<String> stack = new Stack<>();
        stack.push(start);
        List<String> Q = new ArrayList<>();
        Q.add(start);
        List<String> L = new ArrayList<>();
        L.add(start);
        List<String> Ke = new ArrayList<>();
        Map<String, String> farther = new HashMap<>();
        List<String[]> row = new ArrayList<>();
      
        while (!stack.isEmpty()) {
            String node = stack.pop();
            L.remove(node);
            Ke.clear();
            List<String> dinhKeList = danhSach.get(node);
            if (dinhKeList != null) {
                for (String dinhKe : dinhKeList) {
                    Ke.add(dinhKe);
                    if (!farther.containsKey(dinhKe) && !dinhKe.equals(start)) {
                        stack.push(dinhKe);
                        Q.add(dinhKe);
                        farther.put(dinhKe, node);
                        L.add(dinhKe);
                        Collections.reverse(L);
                    }
                }
            }
          
            if (node.equals(end)) {
                List<String> duongDi = new ArrayList<>();
                duongDi.add(node);
                while (!duongDi.get(duongDi.size() - 1).equals(start)) {
                    duongDi.add(farther.get(duongDi.get(duongDi.size() - 1)));
                }
                row.add(new String[] { node, "Tìm thấy-TTKT Dừng", null, null });
                StringBuilder result = new StringBuilder();
                for (int i = duongDi.size() - 1; i >= 0; i--) {
                    result.append(duongDi.get(i));
                    if (i != 0) {
                        result.append(" => ");
                    }
                }
                return tabulate(row, new String[] { "Phát triển TT", "Trạng thái kề", "Danh sách Q", "Danh sách L" })
                        + "\nĐường đi là: " + result.toString();
            }
            row.add(new String[] { node, String.join(", ", Ke), String.join(", ", Q), String.join(", ", L) });
        }
      
        return null;
    }
  
    public static Map<String, List<String>> readFile(String fileName) {
        Map<String, List<String>> danhSach = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length >= 2) {
                    String ptu1 = parts[0];
                    String ptu2 = parts[1];
                    danhSach.putIfAbsent(ptu1, new ArrayList<>());
                    danhSach.get(ptu1).add(ptu2);
                    danhSach.putIfAbsent(ptu2, new ArrayList<>());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return danhSach;
    }
  
    public static void printFile(String fileName, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
  
    public static String tabulate(List<String[]> rows, String[] headers) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(" \t| ", headers)).append("\n");
        for (String[] row : rows) {
            sb.append(String.join(" \t\t| ", row)).append("\n");
        }
        return sb.toString();
    }
}