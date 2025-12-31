package top.bogey.touch_tool.ui.tool;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import top.bogey.touch_tool.R;
import top.bogey.touch_tool.databinding.WidgetToolButtonBinding;
import top.bogey.touch_tool.utils.AppUtil;

public class ToolWidgetButton extends FrameLayout {
    private final WidgetToolButtonBinding binding;

    public ToolWidgetButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = WidgetToolButtonBinding.inflate(LayoutInflater.from(context), this, true);


        try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolWidgetButton)) {
            int icon = typedArray.getResourceId(R.styleable.ToolWidgetButton_icon, 0);
            binding.icon.setImageResource(icon);

            String title = typedArray.getString(R.styleable.ToolWidgetButton_title);
            binding.title.setText(title);

            String link = typedArray.getString(R.styleable.ToolWidgetButton_link);
            binding.link.setOnClickListener(v -> AppUtil.copyToClipboard(context, "tt://open_tool?" + link));
        }
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        binding.getRoot().setOnClickListener(listener);
    }
}
