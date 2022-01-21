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
import fr.ensisa.vallerich.comptidroid.databinding.AccountFragmentBinding;
import fr.ensisa.vallerich.comptidroid.databinding.OperationItemBinding;
import fr.ensisa.vallerich.comptidroid.model.Operation;

public class AccountFragment extends Fragment {

    static private final String TAG = AccountFragment.class.getSimpleName();

    private AccountViewModel mViewModel;
    private AccountFragmentBinding binding;
    private OperationAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false);
        binding.setLifecycleOwner(this);

        binding.operations.setLayoutManager(new LinearLayoutManager(binding.operations.getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(binding.operations.getContext(), DividerItemDecoration.VERTICAL);
        binding.operations.addItemDecoration(divider);

        adapter = new OperationAdapter();
        binding.operations.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        mViewModel.setAccountDao(AppDatabase.get().getAccountDao());
        mViewModel.getOperations().observe(getViewLifecycleOwner(), operations -> adapter.setOperations(operations));
        long id = AccountFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createAccount();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

    private class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.ViewHolder> {
        private List<Operation> operations;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            OperationItemBinding binding;

            public ViewHolder(@NonNull OperationItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.getRoot().setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                System.out.println("Clique " + operations.get(getLayoutPosition()).getOid());
            }
        }

        @NonNull
        @Override
        public OperationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            OperationItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from((viewGroup.getContext())), R.layout.operation_item, viewGroup, false);
            binding.setLifecycleOwner(getViewLifecycleOwner());
            return new OperationAdapter.ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull OperationAdapter.ViewHolder viewHolder, int position) {
            Operation operation = operations.get(position);
            viewHolder.binding.setOperation(operation);
        }

        @Override
        public int getItemCount() {
            return operations == null ? 0 : operations.size();
        }

        public void setOperations(List<Operation> operations) {
            this.operations = operations;
            notifyDataSetChanged();
        }
    }
}