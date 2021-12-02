package fr.ensisa.vallerich.comptidroid.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.databinding.AccountItemBinding;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.AccountListViewModel;

public class AccountListFragment extends Fragment {

    private AccountListViewModel mViewModel;
    private AccountAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.account_list_fragment, container, false);

        RecyclerView list = root.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(list.getContext(), DividerItemDecoration.VERTICAL);
        list.addItemDecoration(divider);

        adapter = new AccountAdapter();
        list.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountListViewModel.class);

        mViewModel.setAccountDao(AppDatabase.get().getAccountDao());
        mViewModel.getAccounts().observe(getViewLifecycleOwner(), adapter::setCollection);
    }

    private class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

        private List<Account> collection;

        public void setCollection(List<Account> accounts) {
            this.collection = collection;
            notifyDataSetChanged();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            AccountItemBinding binding;

            public ViewHolder(@NonNull AccountItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        @NonNull
        @Override
        public AccountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            AccountItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((parent.getContext())), R.layout.account_item, parent, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            ViewHolder vh = new ViewHolder(binding);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
            Account a = collection.get(position);
            holder.binding.setA(a);
        }

        @Override
        public int getItemCount() {
            return collection == null ? 0 : collection.size();
        }
    }
}
