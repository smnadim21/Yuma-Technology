package com.yumatech.smnadim21.tools;

public interface JsonParser {

    public interface Product {
        String short_name = "short_name";
        String product_uuid = "product_uuid";
        String product_type = "product_type";
        String description = "description";
        String properties = "properties";


        public interface Properties {
            String print_order = "print_order";
        }

        String price = "price";

        public interface Price {
            String price = "price";
        }

        String files = "files";
        interface Files{
            String product_uuid = "product_uuid";
            String file_uuid = "file_uuid";

        }
    }
}
