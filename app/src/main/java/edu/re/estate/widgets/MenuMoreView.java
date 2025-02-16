package edu.re.estate.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.re.estate.R;
import edu.re.estate.databinding.MergeMenuMoreViewBinding;
import edu.re.estate.presenters.WebViewActivity;
import edu.re.estate.presenters.post.MyPostListActivity;
import edu.re.estate.presenters.profile.ProfileActivity;

public class MenuMoreView extends FrameLayout {

    public MenuMoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    public MenuMoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public MenuMoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MenuMoreView(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        MergeMenuMoreViewBinding binding = MergeMenuMoreViewBinding.inflate(LayoutInflater.from(context), this);
        if (attributeSet != null) {
            int type;
            try (TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.MenuMoreView)) {
                type = attributes.getInt(R.styleable.MenuMoreView_type, 0);
                switch (type) {
                    case 0:
                        binding.ivMenuMore.setImageResource(R.drawable.ic_help_center);
                        binding.tvMenuMore.setText(R.string.help_center);

                        binding.container.setOnClickListener(v -> {
                            context.startActivity(new Intent(context, WebViewActivity.class)
                                    .putExtra("title", binding.tvMenuMore.getText().toString()));
                        });
                        break;
                    case 1:
                        binding.ivMenuMore.setImageResource(R.drawable.ic_privacy_policy);
                        binding.tvMenuMore.setText(R.string.privacy_policy);
                        binding.container.setOnClickListener(v -> {
                            context.startActivity(new Intent(context, WebViewActivity.class)
                                    .putExtra("title", binding.tvMenuMore.getText().toString()));
                        });
                        break;
                    case 2:
                        binding.ivMenuMore.setImageResource(R.drawable.ic_news_post);
                        binding.tvMenuMore.setText(R.string.news_posted);
                        binding.container.setOnClickListener(v -> {
                            context.startActivity(new Intent(context, MyPostListActivity.class));
                        });
                        break;
                    case 3:
                        binding.ivMenuMore.setImageResource(R.drawable.ic_orders);
                        binding.tvMenuMore.setText(R.string.orders);
                        break;
                    case 4:
                        binding.ivMenuMore.setImageResource(R.drawable.baseline_storefront_24);
                        binding.tvMenuMore.setText(R.string.create_post);
                        break;
                    case 5:
                        binding.ivMenuMore.setImageResource(R.drawable.ic_username);
                        binding.tvMenuMore.setText(R.string.your_profile);
                        binding.container.setOnClickListener(v -> {
                            context.startActivity(new Intent(context, ProfileActivity.class));
                        });
                        break;
                    case 6:
                        binding.ivMenuMore.setImageResource(R.drawable.baseline_logout_24);
                        binding.tvMenuMore.setText(R.string.logout);
                        break;
                    case 7:
                        binding.ivMenuMore.setImageResource(R.drawable.baseline_admin_panel_settings_24);
                        binding.tvMenuMore.setText(R.string.admin);
                        break;
                    default:
                        binding.ivMenuMore.setVisibility(GONE);
                        binding.tvMenuMore.setVisibility(GONE);
                        break;
                }
                attributes.recycle();
            } catch (Exception exception) {
                Log.e("GT404_x", "Error: " + exception.getMessage());
            }
        }
    }
}
