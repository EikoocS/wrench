package tech.cookiepower.wrench;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public class WrenchLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addRepository(
                new RemoteRepository
                        .Builder("maven(aliyun)", "default", "https://maven.aliyun.com/repository/public")
                        .build()
        );
        resolver.addDependency(new Dependency(new DefaultArtifact("org.mozilla:rhino:1.8.0"), null));

        classpathBuilder.addLibrary(resolver);
    }

}
