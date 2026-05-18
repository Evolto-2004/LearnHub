import { defineConfig, type PluginOption } from "vite";
import react from "@vitejs/plugin-react-swc";
import gzipPlugin from "rollup-plugin-gzip";

const stripUseClientDirectives = (): PluginOption => ({
  name: "strip-use-client-directives",
  enforce: "pre",
  transform(code, id) {
    if (!id.includes("node_modules")) {
      return null;
    }
    const nextCode = code.replace(/^(['"])use client\1;?\s*/, "");
    return nextCode === code ? null : { code: nextCode, map: null };
  },
});

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    host: "0.0.0.0",
    port: 3000,
  },
  plugins: [stripUseClientDirectives(), react()],
  build: {
    chunkSizeWarningLimit: 1500,
    rollupOptions: {
      plugins: [gzipPlugin()],
    },
  },
});
