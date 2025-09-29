package view;

import controller.StockController;
import controller.WarehouseController;
import domain.Warehouse;
import domain.WarehouseAdmin;

public class Main {
    public static void main(String[] args) {
        StockController stockController = StockController.getInstance();

        WarehouseAdmin warehouseAdmin = new WarehouseAdmin();
        warehouseAdmin.setWIdx(1);
        stockController.setCurrentUser("WarehouseAdmin", warehouseAdmin);

        stockController.showStockMenu();
    }
}
