package com.example.graphs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.*;

import static java.util.Arrays.fill;

public class HelloController {
    private static int[][] nodeArray;
    private static final Scanner in = new Scanner(System.in);
    private static final ArrayList<String> ways = new ArrayList<>();
    private static int maxLen;
    private static String maxWayRoot;
    private static int middleWidth = 571;
    private static int middleHeight = 292;

    @FXML
    private Button GraphButton;

    @FXML
    private Button ClosestButton;

    @FXML
    private Button LongestButton;

    @FXML
    private Button AllWaysButton;

    @FXML
    private TextField TextF;

    @FXML
    private TextField TextF2;

    @FXML
    private TextField TextF21;

    @FXML
    private TextArea Result;

    @FXML
    private Canvas canvas;

    @FXML
    private Label Source;

    @FXML
    private Label Stock;

    @FXML
    private Label GraphCenter;

    @FXML
    void onGraphButtonClick(ActionEvent event) {
        String value = TextF.getText();
        System.out.println(value);
        int size = (int) Math.sqrt(value.length() / 2 + 1);
        nodeArray = new int[size][size];
        int k = 0, j = 0;
        int i = 0;
        while (true) {
            if (value.charAt(i) != ' ') {
                int startInd = i;
                while (i < value.length() && value.charAt(i) != ' ') {
                    i++;
                }
                nodeArray[k][j] = Integer.parseInt(value.substring(startInd, i));
                if (j == size - 1) {
                    j = 0;
                    if (k == size - 1) break;
                    k++;
                } else j++;
            } else i++;
        }

        Source.setText("Вершина-источник = " + sourceNode());
        Stock.setText("Рез.-вершина = " + stockNode());
        GraphCenter.setText("Центр графа = " + findCenter());
        Draw(size);
    }

    public void Draw(int size) {
        Point[] points = new Point[size];
        GraphicsContext context = canvas.getGraphicsContext2D();
        int r = 10 * size;
        int x = middleWidth, y = middleHeight - r;
        for (int i = 0; i < size; i++) {
            context.setFill(Color.RED);
            points[i] = new Point(x, y);
            context.fillOval(x, y, 30, 30);
            context.setFill(Color.BLACK);
            context.fillText(Integer.toString(i + 1), x + 11, y + 17);
            if (y < middleHeight && x < middleWidth) {
                y += 70;
                x -= 85;
            } else if (y >= middleHeight && x < middleWidth) {
                y += 70;
                x += 90;
            } else if (y >= middleHeight && x >= middleWidth) {
                y -= 70;
                x += 90;
            } else if (y < middleHeight && x >= middleHeight) {
                y -= 70;
                x -= 80;
            }
        }
        DrawLine(points);
    }

    public void DrawLine(Point[] points) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setLineWidth(1);
        context.setStroke(Color.BLUE);

        for (int i = 0; i < nodeArray.length; i++) {
            for (int j = 0; j < nodeArray.length; j++) {
                if (i != j && nodeArray[i][j] != 0) {
                    context.strokeLine(points[i].getX() + 15, points[i].getY() + 30, points[j].getX() + 15, points[j].getY());
                    context.setFill(Color.YELLOW);
                    context.fillOval(points[j].getX() + 10, points[j].getY() - 5, 10, 10);
                    context.setFill(Color.PURPLE);
                    context.fillText(Integer.toString(nodeArray[i][j]), (points[i].getX() + points[j].getX()) / 2 + 15, (points[i].getY() + points[j].getY()) / 2 + 15);
                }
            }
        }
    }

    @FXML
    void onClosestButtonClick(ActionEvent event) {
        if (!Objects.equals(TextF2.getText(), "") && !Objects.equals(TextF21.getText(), "")) {
            Result.clear();
            findClosestWay(Integer.parseInt(TextF2.getText()) - 1, Integer.parseInt(TextF21.getText()) - 1);
        }
    }

    @FXML
    void onLongestButtonClick(ActionEvent event) {
        if (!Objects.equals(TextF2.getText(), "") && !Objects.equals(TextF21.getText(), "")) {
            Result.clear();
            findLongestWay(Integer.parseInt(TextF2.getText()) - 1, Integer.parseInt(TextF21.getText()) - 1);
        }
    }

    @FXML
    void onAllWaysButtonClick(ActionEvent event) {
        if (!Objects.equals(TextF2.getText(), "") && !Objects.equals(TextF21.getText(), "")) {
            Result.clear();
            findAllWays(Integer.parseInt(TextF2.getText()) - 1, Integer.parseInt(TextF21.getText()) - 1);
        }
    }

    public static int sourceNode() {
        boolean findSource;
        for (int i = 0; i < nodeArray.length; i++) {
            findSource = true;
            for (int j = 0; j < nodeArray.length; j++) {
                if (nodeArray[j][i] != 0) {
                    findSource = false;
                    break;
                }
            }
            if (findSource) return i + 1;
        }
        return 0;
    }

    public static int stockNode() {
        boolean findStock;
        for (int i = 0; i < nodeArray.length; i++) {
            findStock = true;
            for (int j = 0; j < nodeArray.length; j++) {
                if (nodeArray[i][j] != 0) findStock = false;
            }
            if (findStock) return i + 1;
        }
        return 0;
    }

    private static int[][] floid() {
        int vNum = nodeArray.length;
        int[][] dist = new int[vNum][vNum]; // dist[i][j] = минимальное_расстояние(i, j)
        for (int i = 0; i < vNum; i++) System.arraycopy(nodeArray[i], 0, dist[i], 0, vNum);
        for (int i = 0; i < vNum; i++)
            for (int j = 0; j < vNum; j++) {
                if (dist[i][j] == 0) dist[i][j] = Integer.MAX_VALUE / 2;
            }
        for (int k = 0; k < vNum; k++)
            for (int i = 0; i < vNum; i++)
                for (int j = 0; j < vNum; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
        return dist;
    }

    public static int findCenter() {
        int[][] closestWays = floid();
        int max = closestWays[0][0];
        for (int i = 1; i < closestWays.length; i++) {
            if (closestWays[i][0] > max) max = closestWays[i][0];
        }

        int minInd = 0;
        int minValue = max;
        for (int j = 1; j < closestWays.length; j++) {
            max = closestWays[0][j];
            for (int i = 1; i < closestWays.length; i++) {
                if (closestWays[i][j] > max) max = closestWays[i][j];
            }
            if (max < minValue) {
                minValue = max;
                minInd = j;
            }
        }
        return minInd + 1;
    }

    public void findClosestWay(int start, int end) {
        int INF = Integer.MAX_VALUE / 2; // "Бесконечность"
        int nodeKol = nodeArray.length;
        /* Алгоритм Дейкстры за O(V^2) */
        boolean[] used = new boolean[nodeKol]; // массив пометок
        int[] dist = new int[nodeKol]; // массив расстояния. dist[v] = минимальное_расстояние(start, v)

        fill(dist, INF); // устанаавливаем расстояние до всех вершин INF
        dist[start] = 0; // для начальной вершины положим 0

        while (true) {
            int closestNode = -1;
            for (int nv = 0; nv < nodeKol; nv++) {    // перебираем вершины
                if (!used[nv] && dist[nv] < INF && (closestNode == -1 || dist[closestNode] > dist[nv]))
                    closestNode = nv;    // выбираем самую близкую непомеченную вершину
            }
            if (closestNode == -1) break; // ближайшая вершина не найдена
            used[closestNode] = true; // помечаем ее
            for (int nv = 0; nv < nodeKol; nv++) {
                if (!used[nv] && nodeArray[closestNode][nv] < INF && nodeArray[closestNode][nv] != 0) // для всех непомеченных смежных
                    dist[nv] = Math.min(dist[nv], dist[closestNode] + nodeArray[closestNode][nv]); // улучшаем оценку расстояния
            }
        }
        int destination = dist[end];
        StringBuilder root = new StringBuilder().append(end + 1).append(">-");
        while (end != start) {
            for (int j = 0; j < nodeKol; j++) {
                if (nodeArray[j][end] != 0) {
                    if (dist[end] == nodeArray[j][end] + dist[j]) {
                        end = j;
                        root.append(j + 1).append(">-");
                        break;
                    }
                }
            }
        }
        root.reverse();
        root.delete(0, 2);
        Result.setText("Самый короткий путь: " + root + ", длина = " + destination);
    }

    public void findLongestWay(int start, int end) {
        maxLen = 0;
        maxWayRoot = "";
        StringBuilder way = new StringBuilder().append(start + 1);
        find(start, end, way, 0);
        StringBuilder result = new StringBuilder(maxWayRoot);
        for (int i = 1; i < result.length(); i += 3) {
            result.insert(i, "->");
        }
        Result.setText("Самый длинный путь: " + result + ", длина = " + maxLen);
    }

    public static void find(int start, int end, StringBuilder way, int wayLen) {
        if (start == end) {
            if (wayLen > maxLen) {
                maxLen = wayLen;
                maxWayRoot = way.toString();
            }
        } else {
            for (int i = 0; i < nodeArray.length; i++) {
                if ((nodeArray[start][i] != 0)) {
                    if ((way.length() < 2) || ((i + 1) != Integer.parseInt(String.valueOf(way.charAt(way.length() - 2))))) {
                        wayLen += nodeArray[start][i];
                        way.append(i + 1);
                        find(i, end, way, wayLen);
                        wayLen -= nodeArray[start][i];
                        if (i + 1 >= 10) way.delete(way.length() - 2, way.length());
                        else way.deleteCharAt(way.length() - 1);
                    }
                }
            }
        }
    }

    public void findAllWays(int start, int end) {
        ways.clear();
        StringBuilder way = new StringBuilder().append(start + 1);
        find(start, end, way);
        sortWays();
    }

    public static void find(int start, int end, StringBuilder way) {
        if (start == end) {
            ways.add(way.toString());
        } else {
            for (int i = 0; i < nodeArray.length; i++) {
                if ((nodeArray[start][i] != 0)) {
                    if ((way.length() < 2) || ((i + 1) != Integer.parseInt(String.valueOf(way.charAt(way.length() - 2))))) {
                        way.append(i + 1);
                        find(i, end, way);
                        if (i + 1 >= 10) way.delete(way.length() - 2, way.length());
                        else way.deleteCharAt(way.length() - 1);
                    }
                }
            }
        }
    }

    public void sortWays() {
        HashMap<String, Integer> waysMap = new HashMap<>();
        for (String way : ways) {
            int length = 0;
            for (int j = 0; j < way.length() - 1; j++) {
                int firInd = Integer.parseInt(way.substring(j, j + 1)) - 1;
                int secInd = Integer.parseInt(way.substring(j + 1, j + 2)) - 1;
                length += nodeArray[firInd][secInd];
            }
            StringBuilder newWay = new StringBuilder();
            for (int i = 0; i < way.length(); i++) {
                newWay.append(way.charAt(i) + "->");
            }
            newWay.delete(newWay.length() - 2, newWay.length());
            waysMap.put(newWay.toString(), length);
        }

        Integer[] arr1 = waysMap.values().toArray(new Integer[waysMap.size()]);
        String[] arr2 = waysMap.keySet().toArray(new String[waysMap.size()]);

        int j;
        for (int i = 1; i < arr1.length; i++) {
            int swap = arr1[i];
            String swap2 = arr2[i];
            for (j = i; j > 0 && swap < arr1[j - 1]; j--) {
                //элементы отсортированного сегмента перемещаем вперёд, если они больше элемента для вставки
                arr1[j] = arr1[j - 1];
                arr2[j] = arr2[j - 1];
            }
            arr1[j] = swap;
            arr2[j] = swap2;
        }

        StringBuilder res = new StringBuilder().append("Все пути и их длины:\n");
        for (int i = 0; i < arr1.length; i++) {
            res.append(arr2[i]).append(" : ").append(arr1[i]).append("\n");
        }
        Result.setText(res.toString());
    }
}