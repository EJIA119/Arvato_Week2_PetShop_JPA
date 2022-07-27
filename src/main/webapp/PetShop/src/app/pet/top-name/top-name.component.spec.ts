import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopNameComponent } from './top-name.component';

describe('TopNameComponent', () => {
  let component: TopNameComponent;
  let fixture: ComponentFixture<TopNameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopNameComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TopNameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
