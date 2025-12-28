package top.bogey.touch_tool.ui.blueprint.card;

public class InputConfigActionAdapter extends CustomActionCardAdapter {
    public InputConfigActionAdapter(ActionCard card) {
        super(card);
    }

    @Override
    public void swap(int from, int to) {
        super.swap(from, to);
        ((InputConfigActionCard) card).swap(from, to);
    }
}
