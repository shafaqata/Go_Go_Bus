package com.technicalskillz.gogobus.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.technicalskillz.gogobus.BusStation.RecyclerviewAdapterStation;
import com.technicalskillz.gogobus.BusStation.RecyclerviewAdapterStationSearch;
import com.technicalskillz.gogobus.BusStation.StationList;
import com.technicalskillz.gogobus.BusStation.Value;
import com.technicalskillz.gogobus.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchFragment extends Fragment {

    Context ctx;
    RecyclerView recyclerView;
    EditText inputSearch;

    RecyclerviewAdapterStationSearch adapterStation;

    public SearchFragment(Context context) {
        ctx = context;
    }

    public SearchFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        inputSearch = view.findViewById(R.id.inputSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        LoadStations();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                adapterStation.getFilter().filter(editable.toString());
            }
        });

        return view;
    }


    private void LoadStations() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://datamall2.mytransport.sg/ltaodataservice/BusStops")
                .method("GET", null)
                .addHeader("AccountKey", "dDFDbRrMR1ujXVKA0+ZvIA==")
                .addHeader("accept", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Gson gson = new Gson();
                            StationList list = gson.fromJson(res, StationList.class);
                            List<Value> stations = list.getValue();

                            Log.d("myT", "run: " + stations.get(0).getBusStopCode());

                            adapterStation = new RecyclerviewAdapterStationSearch(stations, ctx);
                            recyclerView.setAdapter(adapterStation);
                        }
                    });
                }

            }
        });
    }
}