public class Mohammad {
    private static Mohammad ourInstance = new Mohammad();

    public static Mohammad getInstance() {
        return ourInstance;
    }

    private Mohammad() {
    }
}
