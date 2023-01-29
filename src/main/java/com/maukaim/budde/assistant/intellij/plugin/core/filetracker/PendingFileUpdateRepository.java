package com.maukaim.budde.assistant.intellij.plugin.core.filetracker;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Service
@State(name="pendingFileUpdate")
public final class PendingFileUpdateRepository implements PersistentStateComponent<PendingFileUpdateRepository> {
    @OptionTag(converter = PendingFileUpdateMapConverter.class)
    public Map<String, Set<String>> pendingFilePathUpdate = new HashMap<>();

    @Override
    public @Nullable PendingFileUpdateRepository getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PendingFileUpdateRepository state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void addTrackedFilePath(String assistantId, String filePath){
        pendingFilePathUpdate.computeIfAbsent(assistantId,(key)->new HashSet<>());
        System.out.println("Adding to " + assistantId + " filePath : " + filePath);
        pendingFilePathUpdate.get(assistantId).add(filePath);
    }

    public Set<String> popPendingFilePathByAssistantId(String assistantId){
        return Objects.requireNonNullElse(pendingFilePathUpdate.remove(assistantId), Set.of());
    }
}

/**
 * On a les fichiers tracked quand ils sont modified.
 * Quand un Assistant est créé, il faut lui permettre de track des modules.
 * Car pour l'instant personne ne track rien...
 * Une fois que un Assistant track quelque chose, on peut tester le fine tune machin!
 */