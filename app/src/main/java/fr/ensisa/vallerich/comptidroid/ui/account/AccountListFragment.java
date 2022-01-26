package fr.ensisa.vallerich.comptidroid.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.database.DatabaseFeeder;
import fr.ensisa.vallerich.comptidroid.databinding.AccountItemBinding;
import fr.ensisa.vallerich.comptidroid.databinding.AccountListFragmentBinding;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.ui.shared.ItemSwipeCallback;

public class AccountListFragment extends Fragment {

    private AccountListViewModel mViewModel;
    private AccountAdapter adapter;
    private AccountListFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = AccountListFragmentBinding.inflate(inflater, container, false);
        binding.add.setOnClickListener(
            view -> NavHostFragment.findNavController(this).navigate(R.id.action_navigation_accountList_to_account)
        );
        binding.accounts.setLayoutManager(new LinearLayoutManager(binding.accounts.getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration divider = new DividerItemDecoration(binding.accounts.getContext(), DividerItemDecoration.VERTICAL);
        binding.accounts.addItemDecoration(divider);

        binding.accountsHeader.setTypeface(ResourcesCompat.getFont(binding.getRoot().getContext(), R.font.cabin_variable_font_wdth));

        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemSwipeCallback(getContext(), ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, (direction, position) -> {
                    Account account = adapter.accounts.get(position);
                    switch (direction) {
                        case ItemTouchHelper.LEFT:
                            mViewModel.deleteAccount(account);
                            break;
                        case ItemTouchHelper.RIGHT:
                            AccountListFragmentDirections.ActionNavigationAccountListToAccount action =
                                    AccountListFragmentDirections.actionNavigationAccountListToAccount();
                            action.setId(account.getAid());
                            NavHostFragment.findNavController(AccountListFragment.this).navigate(action);
                            break;
                    }
                })
        );

        touchHelper.attachToRecyclerView(binding.accounts);

        adapter = new AccountAdapter();
        binding.accounts.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.accounts_menu, menu);
    }

    private boolean doPopulate(){
        DatabaseFeeder feeder = new DatabaseFeeder();
        feeder.feed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.populate: return doPopulate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountListViewModel.class);

        AppDatabase.isReady().observe(getViewLifecycleOwner(), base -> {
            if (base == null) return;
            mViewModel.setAccountDao(base.getAccountDao());
            mViewModel.getAccounts().observe(getViewLifecycleOwner(), accounts -> adapter.setCollection(accounts));
        });
    }

    private class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

        private List<Account> accounts;

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
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AccountAdapter.ViewHolder holder, int position) {
            holder.binding.setAccount(accounts.get(position));
        }

        public void setCollection(List<Account> accounts) {
            this.accounts = accounts;
            if (accounts.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.accounts.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }
        
        @Override
        public int getItemCount() {
            return accounts == null ? 0 : accounts.size();
        }
    }
}
