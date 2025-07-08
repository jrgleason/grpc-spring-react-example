import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';
import Sitemap from 'vite-plugin-sitemap';
import {fileURLToPath} from 'url';
import path from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

export default defineConfig({
    plugins: [
        react(),
        Sitemap()
    ],
    watch: {
        include: 'src/**'
    },
    build: {
        sourcemap: true
    },
    publicDir: 'public'
});
