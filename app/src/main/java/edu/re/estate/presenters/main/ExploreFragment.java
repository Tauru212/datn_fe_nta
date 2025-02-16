package edu.re.estate.presenters.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.re.estate.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private static ExploreFragment instance;

    public static ExploreFragment getInstance() {
        if (instance == null) {
            instance = new ExploreFragment();
        }
        return instance;
    }

    private FragmentExploreBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void reloadData() {
        double lat = 0L, lng = 0L;
        if (((MainActivity) requireActivity()).currentLocation != null) {
            lat = ((MainActivity) requireActivity()).currentLocation.getLatitude();
            lng = ((MainActivity) requireActivity()).currentLocation.getLongitude();
        }
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl("https://www.google.com/maps/search/@" + lat + "," + lng + ",13z?entry=ttu&g_ep=EgoyMDI0MTIwNC4wIKXMDSoASAFQAw%3D%3D");
        binding.webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
