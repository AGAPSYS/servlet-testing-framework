/*
 * Copyright 2017 Agapsys Tecnologia Ltda-ME.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agapsys.jee;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

public abstract class MappedFileServlet extends AbstractFileServlet {
    
    // <editor-fold desc="STATIC SCOPE" defaultstate="collapsed">
    private static final String KEY_FILE          = MappedFileServlet.class.getName() + ".file";
            static final String KEY_MIME          = MappedFileServlet.class.getName() + ".mime";
    private static final String KEY_LAST_MODIFIED = MappedFileServlet.class.getName() + ".lastModified";
            static final String KEY_IS_DIR        = MappedFileServlet.class.getName() + ".isDir";
    
    public static final String DEFAULT_INDEX_FILENAME = "index.html";

    // Returns the mime-type associated with given file.
    private static String __getMimeType(File file) {
        return _getMimeType(file.getName());
    }
    // </editor-fold>
    
    // prepares the response data associated with given request.
    void _prepareResponseDataOnlyOnce(HttpServletRequest req) {
        if (req.getAttribute(KEY_FILE) == null) {
            
            String pathInfo    = req.getPathInfo();
            
            if (pathInfo == null)
                pathInfo = "";
            
            String fileSeparator = System.getProperty("file.separator");
            String filePath      = pathInfo.replaceAll(Pattern.quote("/"), fileSeparator);
            String mappedDirPath = getMappedDirPath();

            File file = new File(mappedDirPath, filePath);

            boolean isDirectory = file.isDirectory();
            req.setAttribute(KEY_IS_DIR,        isDirectory);
            
            if (isDirectory)
                file = new File(mappedDirPath, getIndexFilename());

            req.setAttribute(KEY_FILE,          file);
            req.setAttribute(KEY_MIME,          __getMimeType(file));
            req.setAttribute(KEY_LAST_MODIFIED, file.exists() ? file.lastModified() : 0);
        }
    }

    @Override
    protected boolean isDirectory(HttpServletRequest req) {
        _prepareResponseDataOnlyOnce(req);
        
        return (boolean) req.getAttribute(KEY_IS_DIR);
    }
    
    @Override
    protected InputStream getTargetInputStreamFor(HttpServletRequest req) throws IOException {
        _prepareResponseDataOnlyOnce(req);
        
        File targetFile = (File) req.getAttribute(KEY_FILE);
        
        if (!targetFile.exists()) {
            return null;
        }
        
        return new FileInputStream(targetFile);
    }

    @Override
    protected long getTargetLastModifiedFor(HttpServletRequest req) {
        _prepareResponseDataOnlyOnce(req);
        
        return (long) req.getAttribute(KEY_LAST_MODIFIED);
    }

    @Override
    protected String getTargetContentTypeFor(HttpServletRequest req) {
        _prepareResponseDataOnlyOnce(req);
        
        return (String) req.getAttribute(KEY_MIME);
    }

    /**
     * Returns the name of the file used when a request points to a directory.
     *
     * @return the name of the file used when a request points to a directory.
     * Default implementation returns {@link MappedFileServlet#DEFAULT_INDEX_FILENAME}.
     */
    protected String getIndexFilename() {
        return DEFAULT_INDEX_FILENAME;
    }
    
    /**
     * Returns the directory path mapped for requests handled by this servlet.
     *
     * @return the directory mapped for requests handled by this servlet.
     */
    protected abstract String getMappedDirPath();
    
}
