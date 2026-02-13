package com.yong.usefulgram.settings;

import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.LaunchActivity;

import java.util.List;

import com.yong.usefulgram.accessibility.AccessibilitySettingsActivity;
import com.yong.usefulgram.helpers.CloudSettingsHelper;
import com.yong.usefulgram.helpers.PasscodeHelper;
import com.yong.usefulgram.helpers.remote.ConfigHelper;
import com.yong.usefulgram.helpers.remote.UpdateHelper;

public class UsefulSettingsActivity extends BaseUsefulSettingsActivity {

    private final List<ConfigHelper.News> news = ConfigHelper.getNews();
    private boolean checkingUpdate = false;

    private int categoriesRow;
    private int generalRow;
    private int appearanceRow;
    private int chatRow;
    private int passcodeRow;
    private int experimentRow;
    private int accessibilityRow;
    private int categories2Row;

    private int aboutRow;
    private int websiteRow;
    private int sourceCodeRow;
    private int about2Row;

    @Override
    public View createView(Context context) {
        View fragmentView = super.createView(context);

        actionBar.createMenu()
                .addItem(0, R.drawable.cloud_sync)
                .setOnClickListener(v -> CloudSettingsHelper.getInstance().showDialog(UsefulSettingsActivity.this));

        return fragmentView;
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == chatRow) {
            presentFragment(new UsefulChatSettingsActivity());
        } else if (position == generalRow) {
            presentFragment(new UsefulGeneralSettingsActivity());
        } else if (position == appearanceRow) {
            presentFragment(new UsefulAppearanceSettings());
        } else if (position == passcodeRow) {
            presentFragment(new UsefulPasscodeSettingsActivity());
        } else if (position == experimentRow) {
            presentFragment(new UsefulExperimentalSettingsActivity());
        } else if (position == accessibilityRow) {
            presentFragment(new AccessibilitySettingsActivity());
        } else if (position == websiteRow) {
            Browser.openUrl(getParentActivity(), "https://nekogram.app");
        } else if (position == sourceCodeRow) {
            Browser.openUrl(getParentActivity(), "https://github.com/Nekogram/Nekogram");
        }
    }

    @Override
    protected BaseListAdapter createAdapter(Context context) {
        return new ListAdapter(context);
    }

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString(R.string.UsefulSettings);
    }

    @Override
    protected String getKey() {
        return "";
    }

    @Override
    protected void updateRows() {
        super.updateRows();

        categoriesRow = addRow("categories");
        generalRow = addRow("general");
        appearanceRow = addRow("appearance");
        chatRow = addRow("chat");
        if (!PasscodeHelper.isSettingsHidden()) {
            passcodeRow = addRow("passcode");
        } else {
            passcodeRow = -1;
        }
        experimentRow = addRow("experiment");
        AccessibilityManager am = (AccessibilityManager) ApplicationLoader.applicationContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am != null && am.isTouchExplorationEnabled()) {
            accessibilityRow = addRow("accessibility");
        } else {
            accessibilityRow = -1;
        }
        categories2Row = addRow();

        aboutRow = addRow("about");
        websiteRow = addRow("website");
        sourceCodeRow = addRow("sourceCode");
        about2Row = addRow();
    }

    private class ListAdapter extends BaseListAdapter {

        public ListAdapter(Context context) {
            super(context);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial, boolean divider) {
            switch (holder.getItemViewType()) {
                case TYPE_SETTINGS: {
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    if (position == websiteRow) {
                        textCell.setTextAndValue(LocaleController.getString(R.string.OfficialSite), "nekogram.app", divider);
                    } else if (position == sourceCodeRow) {
                        textCell.setTextAndValue(LocaleController.getString(R.string.ViewSourceCode), "GitHub", divider);
                    }
                    break;
                }
                case TYPE_HEADER: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == categoriesRow) {
                        headerCell.setText(LocaleController.getString(R.string.Categories));
                    } else if (position == aboutRow) {
                        headerCell.setText(LocaleController.getString(R.string.About));
                    }
                    break;
                }
                case TYPE_DETAIL_SETTINGS: {
                    TextDetailSettingsCell textCell = (TextDetailSettingsCell) holder.itemView;
                    textCell.setMultilineDetail(true);
                    break;
                }
                case TYPE_TEXT: {
                    TextCell textCell = (TextCell) holder.itemView;
                    if (position == chatRow) {
                        textCell.setTextAndIcon(LocaleController.getString(R.string.Chat), R.drawable.msg_discussion, divider);
                    } else if (position == generalRow) {
                        textCell.setTextAndIcon(LocaleController.getString(R.string.General), R.drawable.msg_media, divider);
                    } else if (position == appearanceRow) {
                        textCell.setTextAndIcon(LocaleController.getString(R.string.ChangeChannelNameColor2), R.drawable.msg_theme, divider);
                    } else if (position == passcodeRow) {
                        textCell.setTextAndIcon(LocaleController.getString(R.string.PasscodeUseful), R.drawable.msg_secret, divider);
                    } else if (position == experimentRow) {
                        textCell.setTextAndIcon(LocaleController.getString(R.string.NotificationsOther), R.drawable.msg_fave, divider);
                    } else if (position == accessibilityRow) {
                        textCell.setText(LocaleController.getString(R.string.AccessibilitySettings), divider);
                    }
                    break;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == categories2Row || position == about2Row) {
                return TYPE_SHADOW;
            } else if (position >= websiteRow && position < about2Row) {
                return TYPE_SETTINGS;
            } else if (position == categoriesRow || position == aboutRow) {
                return TYPE_HEADER;
            } else if (position > categoriesRow && position < categories2Row) {
                return TYPE_TEXT;
            }
            return TYPE_SETTINGS;
        }
    }
}
