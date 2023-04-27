package net.orekyuu.nilou;

public final class Endpoints {

    interface HogeControllerEndpoints {
        HogeControllerAllUriBuilder all();

        HogeControllerAllUriBuilder get(String id);
    }

    private final static HogeControllerEndpoints hogeControllerEndpoints = new HogeControllerEndpoints() {
        @Override
        public HogeControllerAllUriBuilder all() {
            HogeControllerAllUriBuilder builder = new HogeControllerAllUriBuilder();
            return builder;
        }

        @Override
        public HogeControllerAllUriBuilder get(String id) {
            HogeControllerAllUriBuilder builder = new HogeControllerAllUriBuilder();
            builder.pathParam("id", id);
            return builder;
        }
    }

    public static HogeControllerEndpoints hogeController() {
        return hogeControllerEndpoints;
    }

}