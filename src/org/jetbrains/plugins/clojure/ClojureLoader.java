package org.jetbrains.plugins.clojure;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.debugger.DebuggerManager;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.refactoring.rename.RenameInputValidatorRegistry;
import com.intellij.util.Function;
import com.intellij.util.containers.HashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.debugger.ClojurePositionManager;
import org.jetbrains.plugins.clojure.refactoring.rename.ClojureRenameInputValidator;
import org.jetbrains.plugins.clojure.refactoring.rename.ClojureSymbolPattern;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: peter
 * Date: Jan 16, 2009
 * Time: 4:34:18 PM
 * Copyright 2007, 2008, 2009 Red Shark Technology
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ClojureLoader implements ApplicationComponent {

  @NotNull
  public static final String CLOJURE_EXTENSION = "clj";

  @NotNull
  public static final Set<String> CLOJURE_EXTENSIONS = new HashSet<String>();

  static {
    CLOJURE_EXTENSIONS.add(CLOJURE_EXTENSION);
  }


  public ClojureLoader() {
  }

  public void initComponent() {
    loadClojure();
  }

  private void loadClojure() {

    ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerAdapter() {
      public void projectOpened(final Project project) {
        DebuggerManager.getInstance(project).registerPositionManagerFactory(new Function<DebugProcess, PositionManager>() {
          public PositionManager fun(DebugProcess debugProcess) {
            return new ClojurePositionManager(debugProcess);
          }
        });
      }
    });

    // register name input validator
    RenameInputValidatorRegistry.getInstance().registerInputValidator(new ClojureSymbolPattern(), new ClojureRenameInputValidator());

  }

  public void disposeComponent() {
  }

  @NotNull
  public String getComponentName() {
    return "clojure.support.loader";
  }

}
