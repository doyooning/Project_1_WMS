package view;

import controller.StockController;
import controller.WarehouseController;
import domain.Warehouse;
import domain.WarehouseAdmin;

public class Main {
    public static void main(String[] args) {
        //StockController stockController = StockController.getInstance();

        //stockController.setCurrentUser("User", null);

        //stockController.showStockSearchMenu();

        WarehouseController warehouseController = WarehouseController.getInstance();
        warehouseController.showWarehouseSearchMenu();
    }
}
