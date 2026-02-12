import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: 'transactions',
        loadComponent: () => import('./features/transactions/pages/transaction-page/transaction-page.component')
            .then(m => m.TransactionPageComponent)
    },
    {
        path: '',
        redirectTo: 'transactions',
        pathMatch: 'full'
    }
];