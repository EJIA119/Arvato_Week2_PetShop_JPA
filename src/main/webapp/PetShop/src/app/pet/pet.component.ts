import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';
import { IPet } from './pet';
import { PetService } from './pet.service';
import { PetFunction } from '../pet/pet.function';

@Component({
  selector: 'app-pet',
  templateUrl: './pet.component.html',
  styleUrls: ['./pet.component.css'],
})
export class PetComponent implements OnInit {
  pageTitle = 'Pet Listing';

  pets: IPet[] = [];

  pet: IPet | undefined;

  errorMessage: string = '';
  sub!: Subscription;

  constructor(
    private petService: PetService,
    private router: Router,
    private location: Location,
    private petFunction : PetFunction
  ) {}

  ngOnInit(): void {
    this.sub = this.petService.findAll().subscribe({
      next: (pets) => (this.pets = pets),
      error: (err) => (this.errorMessage = err),
    });
  }

  pet_findById(){
    this.petFunction.pet_findById();
  }

  pet_topName(){
    this.router.navigate(['/pet/topName']);
  }

  navigateBack(){
    this.location.back();
  }
}
