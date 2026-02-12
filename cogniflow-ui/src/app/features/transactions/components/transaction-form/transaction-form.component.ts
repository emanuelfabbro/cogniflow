import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TransactionRequest } from '../../../../services/transaction.service';

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="needs-validation">
      <div class="mb-3">
        <label class="form-label fw-bold">Account UUID</label>
        <input type="text" class="form-control" formControlName="accountId" 
               [class.is-invalid]="isInvalid('accountId')" placeholder="Ej: a0eebc99-9c0b...">
        <div class="invalid-feedback">UUID requerido.</div>
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Monto</label>
        <div class="input-group">
          <span class="input-group-text">$</span>
          <input type="number" class="form-control" formControlName="amount" 
                 [class.is-invalid]="isInvalid('amount')">
        </div>
        <div class="invalid-feedback">El monto debe ser positivo.</div>
      </div>

      <div class="mb-3">
        <label class="form-label fw-bold">Moneda</label>
        <select class="form-select" formControlName="currency">
          <option value="USD">USD - Dólar</option>
          <option value="EUR">EUR - Euro</option>
          <option value="ARS">ARS - Peso Arg</option>
        </select>
      </div>

      <button type="submit" class="btn btn-primary w-100 py-2" [disabled]="form.invalid">
        <i class="bi bi-shield-lock-fill me-2"></i> Analizar Transacción
      </button>
    </form>
  `
})
export class TransactionFormComponent {
  @Output() formSubmit = new EventEmitter<TransactionRequest>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      accountId: ['', Validators.required],
      amount: [0, [Validators.required, Validators.min(0.01)]],
      currency: ['USD', Validators.required]
    });
  }

  isInvalid(field: string): boolean {
    const control = this.form.get(field);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  onSubmit() {
    if (this.form.valid) {
      this.formSubmit.emit(this.form.value);
    }
  }
}