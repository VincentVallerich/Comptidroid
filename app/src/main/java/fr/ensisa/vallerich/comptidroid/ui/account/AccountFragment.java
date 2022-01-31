package fr.ensisa.vallerich.comptidroid.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.adapters.TextViewBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.R;
import fr.ensisa.vallerich.comptidroid.database.AppDatabase;
import fr.ensisa.vallerich.comptidroid.databinding.AccountFragmentBinding;
import fr.ensisa.vallerich.comptidroid.databinding.OperationItemBinding;
import fr.ensisa.vallerich.comptidroid.model.AccountOperationAssociation;
import fr.ensisa.vallerich.comptidroid.model.Operation;
import fr.ensisa.vallerich.comptidroid.ui.dialog.OperationCreatorFragmentDirections;
import fr.ensisa.vallerich.comptidroid.ui.operation.OperationFragment;

public class AccountFragment extends Fragment {

    private static final String OPERATION = "operation";
    private AccountViewModel mViewModel;
    private AccountFragmentBinding binding;
    private OperationAdapter adapter;
    private Drawable editIcon;
    private Drawable validIcon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getParentFragmentManager().setFragmentResultListener(OPERATION, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey.equals(OPERATION)) {
                    long operationId = result.getLong(OperationFragment.ID);
                    mViewModel.addOperation(operationId);
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false);
        binding.setLifecycleOwner(this);

        binding.setSetName(new TextViewBindingAdapter.OnTextChanged() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setName(s.toString());
            }
        });

        binding.setAddOperation(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccountFragment.this).navigate(R.id.action_account_to_operation_creator);
            }
        });

        binding.editNameIcon.setOnClickListener((view -> {
            if (mViewModel.getEditMode().getValue()) {
                mViewModel.save();
            }
            mViewModel.switchEditMode();
        }));

        binding.operations.setLayoutManager(new LinearLayoutManager(binding.operations.getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(binding.operations.getContext(), DividerItemDecoration.VERTICAL);
        binding.operations.addItemDecoration(divider);

        adapter = new OperationAdapter();
        binding.operations.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        mViewModel.setAccountDao(AppDatabase.get().getAccountDao());
        mViewModel.setOperationDao(AppDatabase.get().getOperationDao());
        mViewModel.getOperations().observe(getViewLifecycleOwner(), operations -> adapter.setOperations(operations));
        mViewModel.getEditMode().observe(getViewLifecycleOwner(), v -> editNameMode(v.booleanValue()));
        mViewModel.getName().observe(getViewLifecycleOwner(), n -> binding.name.setText(n));

        long id = AccountFragmentArgs.fromBundle(getArguments()).getId();
        if (id == 0) {
            mViewModel.createAccount();
        } else {
            mViewModel.setId(id);
        }
        binding.setVm(mViewModel);
    }

    private void editNameMode(boolean edit) {
        if (validIcon == null) {
            validIcon = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.valid_icon);
        }

        if (editIcon == null) {
            editIcon = ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.edit_icon);
        }

        binding.name.setVisibility(edit ? View.GONE : View.VISIBLE);
        binding.editName.setVisibility(edit ? View.VISIBLE : View.GONE);
        binding.editNameIcon.setImageDrawable(edit ? validIcon : editIcon);
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
                AccountFragmentDirections.ActionNavigationAccountToOperation action =
                        AccountFragmentDirections.actionNavigationAccountToOperation();
                action.setId(operations.get(getLayoutPosition()).getOid());
                NavHostFragment.findNavController(AccountFragment.this).navigate(action);
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
            if (operations.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
                binding.operations.setVisibility(View.GONE);
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}