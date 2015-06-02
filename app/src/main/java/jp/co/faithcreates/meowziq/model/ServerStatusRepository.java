package jp.co.faithcreates.meowziq.model;

public class ServerStatusRepository {

    public static String baseUrl;

    public static void loadFromServer(final OnLoadListener listener) {
        new ServerStatusRequest(baseUrl) {
            @Override
            protected void onPostExecute(ServerStatus status) {
                listener.onLoad(status);
            }
        }.execute();
    }

    public interface OnLoadListener {
        void onLoad(ServerStatus status);
    }
}
